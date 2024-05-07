package softwaredesign;


import java.util.*;
import java.net.URL;
import java.io.File;


public class PluginPort {

    private static PluginPort instance;
    private PluginPort() {}
    public static PluginPort getInstance() {
        if(instance == null) {
            instance = new PluginPort();
        }

        return instance;
    }
    private static Plugin[] getClasses(String packageName) throws Exception{
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        assert classLoader != null;
        String path = packageName.replace('.', '/');
        Enumeration<URL> resources = classLoader.getResources(path);
        List<File> dirs = new ArrayList<>();
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            dirs.add(new File(resource.getFile()));
        }
        ArrayList<Plugin> classes = new ArrayList<>();
        for (File directory : dirs) {
            classes.addAll(findClasses(directory, packageName));
        }
        return classes.toArray(new Plugin[0]);
    }


    private static List<Plugin> findClasses(File directory, String packageName) throws Exception {
        List<Plugin> classes = new ArrayList<>();
        if (!directory.exists()) {
            return classes;
        }
        File[] files = directory.listFiles();
        assert files != null;
        for (File file : files) {
            if (file.isDirectory()) {
                assert !file.getName().contains(".");
                classes.addAll(findClasses(file, packageName + "." + file.getName()));
            } else if (file.getName().endsWith(".class")) {
                classes.add((Plugin) Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)).getConstructor().newInstance());
            }
        }
        return classes;
    }

    public void loadPlugins(){
        Plugin[] classes = new Plugin[0];
        try {
            classes = getClasses("softwaredesign.plugins");
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("loaded classes:");
        for (Plugin aClass : classes) {
            System.out.println(aClass);
            Calculator.plugins.add(aClass);
        }

    }

    private List<String> makeRPN(String inStr) throws Exception {

        Stack<String> stack = new Stack<>();
        if(inStr.trim().length() == 0) throw new Exception("empty input string");

        List<String> tokens = new ArrayList<>();
        inStr = inStr.replaceAll("\\s+","");

        //if string starts with negation we add a trailing 0, since subtraction also uses the same symbol
        inStr = inStr.replace("(-", "(0-");
        if (inStr.startsWith("-")){
            inStr = "0" + inStr;
        }

        for(int i = 0; i < inStr.length(); i++) {
            char ch = inStr.charAt(i);
            if(Character.isDigit(ch)){
                String digit = getDigit(inStr.substring(i));
                i += digit.length()-1;
                tokens.add(digit);
            }
            else if (ch == '(')
                stack.push(Character.toString(ch));
            else if (ch == ')') {
                while(!stack.isEmpty() && !Objects.equals(stack.peek(), "("))
                    tokens.add(stack.pop());
                stack.pop();
            }
            //handle ',' when taking input parameters of functions
            else if(ch == ','){
                while(!stack.isEmpty() && !Objects.equals(stack.peek(), "("))
                    tokens.add(stack.pop());
            }
            else {
                //check if current token is an operator or a function
                String operator;
                //get the function name
                if(Character.isLetter(ch)){
                    operator = getFunctionName(inStr.substring(i));
                    i += operator.length()-1;
                }
                else
                    operator = Character.toString(ch);
                // find the operator in the current list of plugins
                Plugin pl = getPlugin(operator);
                if(pl == null)
                    throw new Exception("invalid input: " + operator);

                //put the function/operator in the stack according to their precedence order
                while (!stack.isEmpty() && pl.isLeftAssociative()) {
                    int pr;
                    Plugin temp = getPlugin(stack.peek());
                    if(temp==null)
                        pr = -1;
                    else pr =temp.getPrecedence();
                    if(pl.getPrecedence() <= pr)
                        tokens.add(stack.pop());
                    else break;
                }
                stack.push(operator);
            }
        }
        while (!stack.isEmpty()) {
            if (stack.peek().equals("(")){
                throw new Exception("invalid input - parentheses don't match");
            }
            tokens.add(stack.pop());
        }
        //System.out.println("[debug] rpn: " + tokens);
        return tokens;
    }

    private String evalRPN(List<String> tokens) throws Exception {
        Stack<String> stack = new Stack<>();
        for(String element : tokens){
            if(Character.isDigit(element.charAt(0))){
                stack.push(element);
                continue;
            }

            Plugin op = getPlugin(element);
            StringBuilder input = new StringBuilder();
            assert op != null;
            if(stack.size()<op.getNumParameters()) throw new Exception("invalid number of inputs");
            for(int i=0;i<op.getNumParameters();i++){
                input.append(stack.pop());
                input.append(" ");
            }

            //System.out.println("[debug] operation: " + op.getCommandName() + " " + input);
            String ans = op.executeCommand(input.toString());
            //System.out.println("[debug] ans= " + ans);
            stack.push(ans);
        }
        return stack.pop();
    }

    public String calculate(String inStr) throws Exception {
        List<String> tokens = makeRPN(inStr);
        return evalRPN(tokens);
    }

    private Plugin getPlugin(String ch){
        for(Plugin p: Calculator.plugins){
            if(p.getCommandName().equals(ch)){
                return p;
            }
        }
        return null;
    }


    private String getDigit(String str){
        char ch = str.charAt(0);
        StringBuilder digit = new StringBuilder();
        int i = 0;
        digit.append(ch);

        while(++i <str.length()){
            ch = str.charAt(i);
            if(Character.isDigit(ch)){
                digit.append(ch);
            } else
                break;
        }
        return digit.toString();
    }


    private String getFunctionName(String str){
        char ch = str.charAt(0);
        StringBuilder function = new StringBuilder();
        int i = 0;
        function.append(ch);
        while(++i <str.length()){
            ch = str.charAt(i);
            if(Character.isLetter(ch)){
                function.append(ch);
            } else
                break;
        }
        return function.toString();
    }

}


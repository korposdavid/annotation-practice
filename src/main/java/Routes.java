public class Routes {

    @WebRoute(path = "/index")
    public String onIndex(){
        return "this is the index page";
    }

    @WebRoute(path = "/test")
    public String onTest(){
        return "this is the test page";
    }
}

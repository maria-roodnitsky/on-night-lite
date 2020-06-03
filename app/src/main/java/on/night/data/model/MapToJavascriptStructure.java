package on.night.data.model;

import java.util.HashMap;
import java.util.Map;

public class MapToJavascriptStructure {

    private Map<String, Boolean> fratMap;
    public MapToJavascriptStructure() {
        fratMap = new HashMap<String, Boolean>(){{
            put("trikap", true);
            put("phidelt", true);
            put("chigam", true);
            put("signu", true);
            put("alphachi", true);
            put("zete", true);
            put("beta", true);
            put("sae", true);
            put("tdx", true);
            put("psiu", true);
            put("gdx", true);
            put("heorot", true);
            put("BG", true);
            put("kde", true);
            put("EKT", true);
            put("kd", true);
            put("chidelt", true);
            put("axid", true);
            put("aphi", true);
            put("Kappa", true);
            put("sigdelt", true);
            put("phitau", true);
            put("alphatheta", true);
            put("tabard", true);
        }};
    }

    public void setValue(String fratKey, boolean open) {
        if(fratMap.containsKey(fratKey)) {
            fratMap.put(fratKey, open);
        }
    }

    public boolean getValue(String fratKey) {
        return fratMap.get(fratKey);
    }

    public String toString() {
        String javascript = "let myMap = new Map();\n";
        for (String fratKey: fratMap.keySet()) {
            javascript += "myMap.set(\'" + fratKey + "\'," + fratMap.get(fratKey) + ");\n";
        }
        return javascript;
    }

//	public static void main(String[] args) {
//		MapToJavaScriptStructure mapStructure = new MapToJavaScriptStructure();
//		mapStructure.setValue("bg", false);
//		System.out.println(mapStructure);
//	}

}
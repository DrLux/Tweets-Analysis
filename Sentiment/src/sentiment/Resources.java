
package sentiment;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.spark.api.java.JavaPairRDD;

/**
 *
 * @author Sorrentino Luca
 */
public class Resources {
    private ProcessorSpark ps;
    private Map<String, JavaPairRDD<String, Float>> resources;
    private List<String> common_res;
    private List<String> specific_res;
    private List<String> scored_res;
    
    public Resources(ProcessorSpark spark){
        this.ps = spark;
        resources = new HashMap<String, JavaPairRDD<String, Float>>();
        common_res = new ArrayList<String>();
        specific_res = new ArrayList<String>();
        scored_res = new ArrayList<String>();
        load_resources();
    }
    
    public void load_resources(){
        File common_file = new File( "resources/common/" );
        File scored_file = new File( "resources/with_score/" );
        File specific_file = new File( "resources/specific_sentiment/" );
        
        String[] common_list = common_file.list();
        for (int i = 0; i < common_list.length ; i++){
            common_list[i] = common_list[i].replace(".txt", "");
            common_res.add(common_list[i]);
            resources.put(common_list[i],this.ps.loadResource(common_list[i], "common"));
        }
        
        String[] specific_list = specific_file.list();
        for (int i = 0; i < specific_list.length ; i++){
            specific_list[i] = specific_list[i].replace(".txt","");
            this.specific_res.add(specific_list[i]);
            this.resources.put(specific_list[i], this.ps.loadResource(specific_list[i], "specific_sentiment"));
        }
        
        String[] scored_list = scored_file.list();
        for (int i = 0; i < scored_list.length ; i++){
            scored_list[i] = scored_list[i].replace(".txt","");
            this.scored_res.add(scored_list[i]);
            this.resources.put(scored_list[i],this.ps.loadScoredResource(scored_list[i]));
        }
        
    }
    
    public List<String> getScoredResources(){
        return scored_res;
    }
    
    public List<String> getResources(String sentiment){
        List<String> totalResource = new ArrayList<String>(this.common_res);
        for (String r : this.specific_res){
            if (r.contains(sentiment))
                totalResource.add(r);
        }
        return totalResource;
    }
    
    public JavaPairRDD<String, Float> nameToResources(String filename){
        return this.resources.get(filename);
    }
}

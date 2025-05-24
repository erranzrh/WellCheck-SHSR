// package com.SmartHealthRemoteSystem.SHSR.Symptoms;

// public class Symptoms {
//   private String SymptomID;
//   private String name;
//   private String value;
  
//   public Symptoms(){
//   }

//   public Symptoms(String SymptomID){
//     this.SymptomID=SymptomID;
//   }

//   public Symptoms(String SymptomID, String name){
//     this.SymptomID=SymptomID;
//     this.name=name;
//   }

//   public Symptoms(String SymptomID,String name, String value){
//     this.SymptomID=SymptomID;
//     this.name=name;
//     this.value=value;
//   }

//   public void setSymptomID(String SymptomID){
//     this.SymptomID=SymptomID;
//   }

//   public String getSymptomID(){
//     return SymptomID;
//   }

//   public void setName(String name){
//     this.name=name;
//   }

//   public String getName(){
//     return name;
//   }

//   public void setValue(String value){
//     this.value=value;
//   }

//   public String getValue(){
//     return value;
//   }
// }



//MongoDb//
package com.SmartHealthRemoteSystem.SHSR.Symptoms;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Symptom")
public class Symptom {
    @Id
    private String id;
    private String name;
    private String value;
    private Integer weight;  // <-- Add this

    public Symptom() {}

    public Symptom(String name, String value, Integer weight) {
        this.name = name;
        this.value = value;
        this.weight = weight;
    }

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }

    public Integer getWeight() { return weight; }
    public void setWeight(Integer weight) { this.weight = weight; }
}

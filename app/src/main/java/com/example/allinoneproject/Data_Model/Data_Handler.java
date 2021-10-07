package com.example.allinoneproject;

import com.google.firebase.firestore.Exclude;

import java.util.ArrayList;

public class Data_Handler {
    private String currentUid;
    private String currentUserName;
    private String desc;
    private String documentID;
    private ArrayList<String> uriList;
    private ArrayList<String> thumbUriList;



//    private String imageUri;


    Data_Handler() {

    }

/*    public Data_Handler(String currentUid, String currentUserName, String desc, String documentID, String imageUri ) {
        this.currentUid = currentUid;
        this.currentUserName = currentUserName;
        this.desc = desc;
        this.documentID = documentID;
        this.imageUri = imageUri;
    }

    public String getCurrentUid() {
        return currentUid;
    }

    public void setCurrentUid(String currentUid) {
        this.currentUid = currentUid;
    }

    public String getCurrentUserName() {
        return currentUserName;
    }

    public void setCurrentUserName(String currentUserName) {
        this.currentUserName = currentUserName;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDocumentID() {
        return documentID;
    }

    public void setDocumentID(String documentID) {
        this.documentID = documentID;
    }*/

    public Data_Handler(String currentUid, String currentUserName, String desc, ArrayList<String> uriList, ArrayList<String> thumbUriList) {
        this.currentUserName = currentUserName;
        this.desc = desc;
//        this.imageUri = imageUri;
        this.currentUid = currentUid;
        this.uriList = uriList;
        this.thumbUriList = thumbUriList;
    }

    public ArrayList<String> getThumbUriList() {
        return thumbUriList;
    }

    public ArrayList<String> getUriList() {
        return uriList;
    }

//    public String getImageUri() {
//        return imageUri;
//    }
    public String getCurrentUserName() {
        return currentUserName;
    }

    public String getDesc() {
        return desc;
    }

    @Exclude
    public String getDocumentID() {
        return documentID;
    }

    public void setDocumentID(String documentID) {
        this.documentID = documentID;
    }

    public String getCurrentUid() {
        return currentUid;
    }

/*        public ArrayList<String> getUriList() {
        return uriList;
    }
  */
}

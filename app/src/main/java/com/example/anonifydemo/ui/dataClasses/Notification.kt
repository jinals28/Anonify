package com.example.anonifydemo.ui.dataClasses

public class Notification {
     var userid: String? = null
     var text: String? = null
    var postid: String? = null
     var isPost = false
    constructor()

    constructor(userid: String?, text: String?, postid: String?, isPost: Boolean) {
        this.userid = userid
        this.text = text
        this.postid = postid
        this.isPost = isPost
    }
}
package com.example.chatappdemo.model

class User {
    var name : String? = null
    var email : String? = null
    var uId : String? = null

    constructor(){}

    constructor(name: String?, email: String?, uId: String){
        this.name = name
        this.email = email
        this.uId = uId
    }

}
package com.firebase.ecommerce.core

import java.util.UUID


object Constants {
    //App
    const val TAG = "AppTag"

    //References
    const val PROFILE = "Profile"

    //Image fields
    const val URL = "url"
    const val CREATED_AT = "createdAt"

    //User details
    val UidRandom = UUID.randomUUID().toString()
    const val UID = "profile_image"


    //File names
    const val PROFILE_IMAGE_NAME = "$UID.jpg"

    //File types
    const val ALL_IMAGES = "image/*"

    //Actions
    const val OPEN_GALLERY = "Open Gallery"

    //Messages
    const val IMAGE_SUCCESSFULLY_ADDED_MESSAGE = "Image successfully added."
    const val DISPLAY_IT_MESSAGE = "Display it?"
}
package com.example.metindogun_challenge.utils

import com.example.metindogun_challenge.data.model.Photo


fun Photo.getImagePath(): String{
    return "https://live.staticflickr.com/$server/${id}_$secret.jpg"
}
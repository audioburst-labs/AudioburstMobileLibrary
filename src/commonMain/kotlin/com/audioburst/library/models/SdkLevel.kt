package com.audioburst.library.models

enum class SdkLevel(internal val levelName: String) {
    Core("core"),
    Controller("controller"),
    Full("full"),
    Banner("banner"),
    Button("button"),
    Player("player");
}
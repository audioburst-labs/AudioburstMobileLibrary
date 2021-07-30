package com.audioburst.library.models

enum class SdkLevel(internal val levelName: String) {
    Core("core"),
    Controller("controller"),
    Ui("ui"),
    Player("player");
}
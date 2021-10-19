package roger.brian.android81

import androidx.room.TypeConverter

class MyTypeConverter {

    @TypeConverter
    fun restoreList(listOfString: String): List<String> {
        return listOfString.split(";")
    }

    @TypeConverter
    fun saveList(listOfString: List<String>): String {
        return listOfString.joinToString(";")
    }

}
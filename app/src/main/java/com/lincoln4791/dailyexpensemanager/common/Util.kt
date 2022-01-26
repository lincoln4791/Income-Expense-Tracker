package com.lincoln4791.dailyexpensemanager.common

class Util(){
    companion object{
        fun getMonthNameFromMonthNumber(monthNumber : String):String{
            var monthName = ""
            if(monthNumber=="01"){
                monthName = "January"
            }
            else if(monthNumber=="02"){
                monthName="February"
            }
            else if(monthNumber=="03"){
                monthName="March"
            }
            else if(monthNumber=="04"){
                monthName="April"
            }
            else if(monthNumber=="05"){
                monthName="May"
            }
            else if(monthNumber=="06"){
                monthName="June"
            }
            else if(monthNumber=="07"){
                monthName="July"
            }
            else if(monthNumber=="08"){
                monthName="August"
            }
            else if(monthNumber=="09"){
                monthName="September"
            }
            else if(monthNumber=="10"){
                monthName="October"
            }
            else if(monthNumber=="11"){
                monthName="November"
            }
            else if(monthNumber=="12"){
                monthName="December"
            }
            else{
                monthName="Blank"
            }


            return monthName
        }
    }
}
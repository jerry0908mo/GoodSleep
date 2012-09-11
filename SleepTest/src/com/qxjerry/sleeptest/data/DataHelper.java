package com.qxjerry.sleeptest.data;

public class DataHelper {

    private   static DataHelper   mDataHelper;

        private   DataHelper(){

        }

        public   static  DataHelper     getInstance(){
            if(null == mDataHelper){
                mDataHelper =   new  DataHelper();
            }
            return  mDataHelper;
        }

        public   void  setWakeupTime(){

        }

        public   void   setGotobedTime(){

        }


}

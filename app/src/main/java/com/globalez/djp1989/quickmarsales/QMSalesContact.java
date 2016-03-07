package com.globalez.djp1989.quickmarsales;

import com.cloudmine.api.db.LocallySavableCMObject;

/**
 * Created by djp1989 on 2/24/16.
 */
public class QMSalesContact extends LocallySavableCMObject {

        private String name;
        private String phoneNumber;
        private String emailAddress;

        //there must be a no-args constructor for deserializing to work
        QMSalesContact(){}
        public QMSalesContact(String name, String phoneNumber, String emailAddress) {
            this.name = name;
            this.phoneNumber = phoneNumber;
            this.emailAddress = emailAddress;
        }

        //Your getter and setters determine what gets serialized and what doesn't
        public String getName() {return name;}
        public void setName(String name) {this.name = name;}
        public String getPhoneNumber() {return phoneNumber;}
        public void setPhoneNumber(String phoneNumber) {this.phoneNumber = phoneNumber;}
        public String getEmailAddress() {return emailAddress;}
        public void setEmailAddress(String emailAddress) {this.emailAddress = emailAddress;}
}

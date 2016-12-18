package com.moodmedia.tmobiles.webclient;

/**
 * This interface is a response handler contract between com.sapient.mobility.sncore.reference.model, datamanager and controller
 */
public interface ResponseHandler <M extends Model>{

        void onRequestFailure(String errorMessage);

        void onRequestSuccess(M model);

}

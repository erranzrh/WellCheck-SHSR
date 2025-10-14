


//MongoDB//
package com.SmartHealthRemoteSystem.SHSR.ProvideDiagnosis;

import java.time.Instant;

public class Diagnosis {

    private String diagnosisId; // Unique ID for each diagnosis
    private String patientId;   // Redundant but useful for clarity
    private String doctorId;    // Doctor who added it
    private String diagnosis;   // Disease/Condition name
    private String diagnosisConfirmation; // e.g., "confirmed", "pending"
    private String remarks;     // Doctor’s remarks
    private String timestamp;   // ISO timestamp (Instant.toString())

    // Default constructor
    public Diagnosis() {}

    // Constructor without ID
    public Diagnosis(String patientId, String doctorId, String diagnosis,
                     String diagnosisConfirmation, String remarks) {
        this.diagnosisId = null; // To be set when saved
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.diagnosis = diagnosis;
        this.diagnosisConfirmation = diagnosisConfirmation;
        this.remarks = remarks;
        this.timestamp = Instant.now().toString();
    }

    // Getters and setters
    public String getDiagnosisId() {
        return diagnosisId;
    }

    public void setDiagnosisId(String diagnosisId) {
        this.diagnosisId = diagnosisId;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getDiagnosisConfirmation() {
        return diagnosisConfirmation;
    }

    public void setDiagnosisConfirmation(String diagnosisConfirmation) {
        this.diagnosisConfirmation = diagnosisConfirmation;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}

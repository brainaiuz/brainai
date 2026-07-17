    package com.edatasite.workforce.gwt.core.client.rpc;

    import com.google.gwt.user.client.rpc.IsSerializable;

    public class VacancyQuestionTableItem implements IsSerializable {

        private Integer fieldId;
        private Integer questionReferenceId;
        private ReferenceItem questionReference;

        public VacancyQuestionTableItem() {
        }

        public Integer getFieldId() {return fieldId;}

        public void setFieldId(Integer fieldId) {this.fieldId = fieldId;}

        public Integer getQuestionReferenceId() {return questionReferenceId;}

        public void setQuestionReferenceId(Integer questionReferenceId) {this.questionReferenceId = questionReferenceId;}

        public ReferenceItem getQuestionReference() {
            return questionReference;
        }

        public void setQuestionReference(ReferenceItem questionReference) {
            this.questionReference = questionReference;
            this.questionReferenceId = questionReference.getObjectID();
        }
    }

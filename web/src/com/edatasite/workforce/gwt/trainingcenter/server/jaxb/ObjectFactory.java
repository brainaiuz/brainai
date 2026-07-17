package com.edatasite.workforce.gwt.trainingcenter.server.jaxb;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.namespace.QName;
import java.math.BigInteger;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _SchoolName_QNAME = new QName("urn:schemas-examview-com:testmgr:class", "school-name");
    private final static QName _PointsEarned_QNAME = new QName("urn:schemas-examview-com:testmgr:results", "points-earned");
    private final static QName _PointsPossible_QNAME = new QName("urn:schemas-examview-com:testmgr:assignment", "points-possible");
    private final static QName _Gender_QNAME = new QName("urn:schemas-examview-com:testmgr:student", "gender");
    private final static QName _LastName_QNAME = new QName("urn:schemas-examview-com:testmgr:student", "last-name");
    private final static QName _ShowTerm_QNAME = new QName("urn:schemas-examview-com:testmgr:preferences", "show-term");
    private final static QName _TestFile_QNAME = new QName("urn:schemas-examview-com:testmgr:assignment", "test-file");
    private final static QName _OnlineId_QNAME = new QName("urn:schemas-examview-com:testmgr:assignment", "online-id");
    private final static QName _Custom3_QNAME = new QName("urn:schemas-examview-com:testmgr:preferences", "custom-3");
    private final static QName _Custom2_QNAME = new QName("urn:schemas-examview-com:testmgr:preferences", "custom-2");
    private final static QName _Custom1_QNAME = new QName("urn:schemas-examview-com:testmgr:preferences", "custom-1");
    private final static QName _InstructorName_QNAME = new QName("urn:schemas-examview-com:testmgr:class", "instructor-name");
    private final static QName _AssignmentName_QNAME = new QName("urn:schemas-examview-com:testmgr:assignment", "assignment-name");
    private final static QName _Reference_QNAME = new QName("urn:schemas-examview-com:testmgr:assignment", "reference");
    private final static QName _City_QNAME = new QName("urn:schemas-examview-com:testmgr:class", "city");
    private final static QName _Text_QNAME = new QName("urn:schemas-examview-com:testmgr:results", "text");
    private final static QName _ClassName_QNAME = new QName("urn:schemas-examview-com:testmgr:class", "class-name");
    private final static QName _EndDate_QNAME = new QName("urn:schemas-examview-com:testmgr:results", "end-date");
    private final static QName _StudentId_QNAME = new QName("urn:schemas-examview-com:testmgr:student", "student-id");
    private final static QName _DeliveryMethod_QNAME = new QName("urn:schemas-examview-com:testmgr:assignment", "delivery-method");
    private final static QName _State_QNAME = new QName("urn:schemas-examview-com:testmgr:class", "state");
    private final static QName _Answer_QNAME = new QName("urn:schemas-examview-com:testmgr:assignment", "answer");
    private final static QName _LearningObjective_QNAME = new QName("urn:schemas-examview-com:testmgr:assignment", "learning-objective");
    private final static QName _Term_QNAME = new QName("urn:schemas-examview-com:testmgr:assignment", "term");
    private final static QName _DefaultTerm_QNAME = new QName("urn:schemas-examview-com:testmgr:preferences", "default-term");
    private final static QName _NumChoices_QNAME = new QName("urn:schemas-examview-com:testmgr:assignment", "num-choices");
    private final static QName _AssignmentDate_QNAME = new QName("urn:schemas-examview-com:testmgr:assignment", "assignment-date");
    private final static QName _Category_QNAME = new QName("urn:schemas-examview-com:testmgr:assignment", "category");
    private final static QName _FirstName_QNAME = new QName("urn:schemas-examview-com:testmgr:student", "first-name");
    private final static QName _StartDate_QNAME = new QName("urn:schemas-examview-com:testmgr:results", "start-date");
    private final static QName _NumAttempts_QNAME = new QName("urn:schemas-examview-com:testmgr:results", "num-attempts");
    private final static QName _PadId_QNAME = new QName("urn:schemas-examview-com:testmgr:student", "pad-id");
    private final static QName _ShowNclb_QNAME = new QName("urn:schemas-examview-com:testmgr:preferences", "show-nclb");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link StateStandard }
     * 
     */
    public StateStandard createStateStandard() {
        return new StateStandard();
    }

    /**
     * Create an instance of {@link Results }
     * 
     */
    public Results createResults() {
        return new Results();
    }

    /**
     * Create an instance of {@link Assignment }
     * 
     */
    public Assignment createAssignment() {
        return new Assignment();
    }

    /**
     * Create an instance of {@link Question }
     * 
     */
    public Question createQuestion() {
        return new Question();
    }

    /**
     * Create an instance of {@link ResponseList }
     * 
     */
    public ResponseList createResponseList() {
        return new ResponseList();
    }

    /**
     * Create an instance of {@link DisplayScore }
     * 
     */
    public DisplayScore createDisplayScore() {
        return new DisplayScore();
    }

    /**
     * Create an instance of {@link StudentSort }
     * 
     */
    public StudentSort createStudentSort() {
        return new StudentSort();
    }

    /**
     * Create an instance of {@link Preferences }
     * 
     */
    public Preferences createPreferences() {
        return new Preferences();
    }

    /**
     * Create an instance of {@link StudentList }
     * 
     */
    public StudentList createStudentList() {
        return new StudentList();
    }

    /**
     * Create an instance of {@link AssignmentList }
     * 
     */
    public AssignmentList createAssignmentList() {
        return new AssignmentList();
    }

    /**
     * Create an instance of {@link Response }
     * 
     */
    public Response createResponse() {
        return new Response();
    }

    /**
     * Create an instance of {@link AssignmentSort }
     * 
     */
    public AssignmentSort createAssignmentSort() {
        return new AssignmentSort();
    }

    /**
     * Create an instance of {@link ResultsList }
     * 
     */
    public ResultsList createResultsList() {
        return new ResultsList();
    }

    /**
     * Create an instance of {@link Class }
     * 
     */
    public ParentClass createClass() {
        return new ParentClass();
    }

    /**
     * Create an instance of {@link CategoryName }
     * 
     */
    public CategoryName createCategoryName() {
        return new CategoryName();
    }

    /**
     * Create an instance of {@link Student }
     * 
     */
    public Student createStudent() {
        return new Student();
    }

    /**
     * Create an instance of {@link QuestionList }
     * 
     */
    public QuestionList createQuestionList() {
        return new QuestionList();
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:schemas-examview-com:testmgr:class", name = "school-name")
    public JAXBElement<String> createSchoolName(String value) {
        return new JAXBElement<>(_SchoolName_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link java.math.BigInteger }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:schemas-examview-com:testmgr:results", name = "points-earned")
    public JAXBElement<BigInteger> createPointsEarned(BigInteger value) {
        return new JAXBElement<>(_PointsEarned_QNAME, BigInteger.class, null, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link java.math.BigInteger }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:schemas-examview-com:testmgr:assignment", name = "points-possible")
    public JAXBElement<BigInteger> createPointsPossible(BigInteger value) {
        return new JAXBElement<>(_PointsPossible_QNAME, BigInteger.class, null, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:schemas-examview-com:testmgr:student", name = "gender")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createGender(String value) {
        return new JAXBElement<>(_Gender_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:schemas-examview-com:testmgr:student", name = "last-name")
    public JAXBElement<String> createLastName(String value) {
        return new JAXBElement<>(_LastName_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:schemas-examview-com:testmgr:preferences", name = "show-term")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createShowTerm(String value) {
        return new JAXBElement<>(_ShowTerm_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:schemas-examview-com:testmgr:assignment", name = "test-file")
    public JAXBElement<String> createTestFile(String value) {
        return new JAXBElement<>(_TestFile_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:schemas-examview-com:testmgr:assignment", name = "online-id")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createOnlineId(String value) {
        return new JAXBElement<>(_OnlineId_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:schemas-examview-com:testmgr:preferences", name = "custom-3")
    public JAXBElement<String> createCustom3(String value) {
        return new JAXBElement<>(_Custom3_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:schemas-examview-com:testmgr:preferences", name = "custom-2")
    public JAXBElement<String> createCustom2(String value) {
        return new JAXBElement<>(_Custom2_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:schemas-examview-com:testmgr:preferences", name = "custom-1")
    public JAXBElement<String> createCustom1(String value) {
        return new JAXBElement<>(_Custom1_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:schemas-examview-com:testmgr:class", name = "instructor-name")
    public JAXBElement<String> createInstructorName(String value) {
        return new JAXBElement<>(_InstructorName_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:schemas-examview-com:testmgr:assignment", name = "assignment-name")
    public JAXBElement<String> createAssignmentName(String value) {
        return new JAXBElement<>(_AssignmentName_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:schemas-examview-com:testmgr:assignment", name = "reference")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createReference(String value) {
        return new JAXBElement<>(_Reference_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:schemas-examview-com:testmgr:class", name = "city")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createCity(String value) {
        return new JAXBElement<>(_City_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:schemas-examview-com:testmgr:results", name = "text")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createText(String value) {
        return new JAXBElement<>(_Text_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:schemas-examview-com:testmgr:class", name = "class-name")
    public JAXBElement<String> createClassName(String value) {
        return new JAXBElement<>(_ClassName_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:schemas-examview-com:testmgr:results", name = "end-date")
    public JAXBElement<String> createEndDate(String value) {
        return new JAXBElement<>(_EndDate_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link java.math.BigInteger }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:schemas-examview-com:testmgr:student", name = "student-id")
    public JAXBElement<BigInteger> createStudentId(BigInteger value) {
        return new JAXBElement<>(_StudentId_QNAME, BigInteger.class, null, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:schemas-examview-com:testmgr:assignment", name = "delivery-method")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createDeliveryMethod(String value) {
        return new JAXBElement<>(_DeliveryMethod_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:schemas-examview-com:testmgr:class", name = "state")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createState(String value) {
        return new JAXBElement<>(_State_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:schemas-examview-com:testmgr:assignment", name = "answer")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createAnswer(String value) {
        return new JAXBElement<>(_Answer_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:schemas-examview-com:testmgr:assignment", name = "learning-objective")
    public JAXBElement<String> createLearningObjective(String value) {
        return new JAXBElement<>(_LearningObjective_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link java.math.BigInteger }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:schemas-examview-com:testmgr:assignment", name = "term")
    public JAXBElement<BigInteger> createTerm(BigInteger value) {
        return new JAXBElement<>(_Term_QNAME, BigInteger.class, null, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link java.math.BigInteger }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:schemas-examview-com:testmgr:preferences", name = "default-term")
    public JAXBElement<BigInteger> createDefaultTerm(BigInteger value) {
        return new JAXBElement<>(_DefaultTerm_QNAME, BigInteger.class, null, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link java.math.BigInteger }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:schemas-examview-com:testmgr:assignment", name = "num-choices")
    public JAXBElement<BigInteger> createNumChoices(BigInteger value) {
        return new JAXBElement<>(_NumChoices_QNAME, BigInteger.class, null, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:schemas-examview-com:testmgr:assignment", name = "assignment-date")
    public JAXBElement<String> createAssignmentDate(String value) {
        return new JAXBElement<>(_AssignmentDate_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:schemas-examview-com:testmgr:assignment", name = "category")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createCategory(String value) {
        return new JAXBElement<>(_Category_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:schemas-examview-com:testmgr:student", name = "first-name")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createFirstName(String value) {
        return new JAXBElement<>(_FirstName_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:schemas-examview-com:testmgr:results", name = "start-date")
    public JAXBElement<String> createStartDate(String value) {
        return new JAXBElement<>(_StartDate_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link java.math.BigInteger }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:schemas-examview-com:testmgr:results", name = "num-attempts")
    public JAXBElement<BigInteger> createNumAttempts(BigInteger value) {
        return new JAXBElement<>(_NumAttempts_QNAME, BigInteger.class, null, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link java.math.BigInteger }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:schemas-examview-com:testmgr:student", name = "pad-id")
    public JAXBElement<BigInteger> createPadId(BigInteger value) {
        return new JAXBElement<>(_PadId_QNAME, BigInteger.class, null, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:schemas-examview-com:testmgr:preferences", name = "show-nclb")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createShowNclb(String value) {
        return new JAXBElement<>(_ShowNclb_QNAME, String.class, null, value);
    }

}

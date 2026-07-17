package com.edatasite.workforce.gwt.ai;

public class PromptBuilder {

    // DO NOT CHANGE THE PROMPT!!!!
    public static String buildSystemPrompt(String locale) {

        String languageName = switch (locale.toLowerCase()) {
            case "ru" -> "Russian";
            case "uz" -> "Uzbek";
            default -> "English";
        };

        return """
                You are an expert HR Data Analyst specializing in writing professional job descriptions and performance metrics.
                
                CRITICAL LANGUAGE RULE: Generate ALL content in""" + languageName + """
                
                
                OUTPUT FORMAT:
                1. All values are strings.
                2. You MUST return valid JSON only. Do not add any Markdown formatting (e.g., ```json), explanations, or extra text outside the JSON.
                3. The JSON must contain exactly these keys (no more, no less, and no null values):
                
                {
                 "salaryBasis": "string",
                 "positionDescription": "string",
                 "jobRequirements": "string",
                 "responsibilities": "string",
                 "measuringEmployeePerformance": "string",
                 "personalQualities": "string",
                 "knowledge": "string"
                }
                
                CONTENT GUIDELINES:
                1. salaryBasis MUST be plain text only. Do not use any HTML tags in this field.
                2. Format all text values using HTML tags. Wrap distinct paragraphs in <p>...</p>. Do not use \\n for newlines; use <p> tags for separation instead.
                3. Lists: If a section requires a list, use standard HTML <p>- </p> or <p>• </p>.
                
                
                FIELD DEFINITIONS:
                - salaryBasis: A short summary of the position (1–3 sentences).
                
                - positionDescription: A detailed, attractive full job description (300–550 words). Include department context, the purpose of the role, key duties, and how it contributes to the organization. Use engaging language. Format with <p> tags for paragraphs. Example: <p>Introduction paragraph here.</p><p>Detailed duties paragraph.</p>
                - jobRequirements: List of required qualifications, skills, and experience (250–550 words). Use <ul><li> for 8–15 items, with descriptive text in each. Example: <ul><li>Bachelor's degree in Computer Science or related field, with at least 3 years of professional experience.</li><li>Proven expertise in Java and Spring Boot frameworks.</li></ul>
                - responsibilities: List of key job duties (250–550  words). Make them action-oriented. Use <ul><li> for 8–15 items. Example: <ul><li>Design and implement software features to meet business needs.</li><li>Collaborate with cross-functional teams to ensure project success.</li></ul>
                - personalQualities: List of desired soft skills and traits (8–15 items, 150–400 words total). Use <p>- </p>. Example: <p>- Strong problem-solving abilities.</p><p>- Excellent communication skills, both verbal and written.</p><p>- Ability to work independently and in a team environment.</p>
                
                - knowledge: List of required technical or domain knowledge (10–20 items, 150–400 words total). Use <ul><li>. Example: <ul><li>Proficiency in SQL and database management.</li><li>Understanding of agile methodologies like SCRUM.</li><li>Advanced skills in Microsoft Excel for data analysis.</li><li>Familiarity with cloud platforms such as AWS.</li><li>Knowledge of version control systems like Git.</li><li>Expertise in API development.</li></ul>
                - measuringEmployeePerformance: Analyze the responsibilities and create 5–8 distinct, quantifiable KPIs. They must be specific, measurable, and derived directly from responsibilities/requirements. Use <ul><li> for the list.
                  Examples: <ul><li>Daily reports sent to the Telegram group during the 26 working days.</li><li>Minimum 50 outbound calls per day with at least 20% conversion rate.</li><li>Resolve 95% of customer tickets within 24 hours.</li><li>Achieve quarterly sales target of $100,000.</li><li>Complete 100% of assigned tasks on time per sprint.</li></ul>
                
                """;
    }


    public static String buildUserPrompt(String pName, String pDesc, String dName, String dDesc) {
        String safePName = pName == null ? "N/A" : pName;
        String safePDesc = pDesc == null ? "N/A" : pDesc;
        String safeDName = dName == null ? "N/A" : dName;
        String safeDDesc = dDesc == null ? "N/A" : dDesc;

        return """
                Generate a position specification based on the following input:
                
                POSITION DETAILS
                Title: %s
                Context: %s
                
                DEPARTMENT DETAILS
                Title: %s
                Context: %s
                
                Use the provided title and contexes to create professional, realistic content.
                """.formatted(safePName, safePDesc, safeDName, safeDDesc);
    }
}

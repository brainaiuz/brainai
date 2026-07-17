import org.xhtmlrenderer.pdf.ITextRenderer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
/* This Servlet accepts a HTML String Fragment and Converts it into PDF file
Using iText and Flying Saucer */
public class HTMLtoPDFServlet extends HttpServlet {
    public HTMLtoPDFServlet() {

    }
    /* We use a doGet method and invoke it internally from a doPost */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        OutputStream out = response.getOutputStream(); /* Get the output stream from the response object */
        try {
            /* Set the output response type */
            /* If the below property is not set, the browser will simply dump the PDF as a text file as an output */
            response.setContentType("application/pdf"); /* We have to set this response type for the browser to open the PDF properly */
            ITextRenderer renderer = new ITextRenderer();
            /* Accept the input provided by user in the HTML form */
            renderer.setDocumentFromString(request.getParameter("InputData"));
            renderer.layout();
            /* Write the converted PDF output to the output stream */
            renderer.createPDF(out);
        }
        catch (Exception e) {
            e.printStackTrace(); /* Throw exceptions to log files */
        }
        finally {
            out.close();/* Close the output stream */
        }
    }
    /* The doPost method provided below would be invoked when you
post the data in the HTML form (that contains a HTML string) to
the servlet. With this HTML form, we will invoke doGet and convert the 
HTML string to PDF file */
    public void doPost(HttpServletRequest request,
                       HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
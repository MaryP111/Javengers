package gr.ntua.ece.webapp.product;

/*
 * import javax.servlet.*;
 * import javax.servlet.*;
 * import javax.servlet.http.*;
 */

import javax.servlet.FilterRegistration;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/*
 * Servlets are used to add dynamic content to a web server.
 * They act as a middle layer between requests coming from a Web browser
 * and the database.
 */

/*
 * In the Model-View-Control Software Architecture
 * servlets implement the control section
 */


@WebServlet("/UserControllerServlet")

/* Extend HttpServlet class */

public class ProductControllerServlet extends HttpServlet {

  private static final long serialVersionUID = 1L;

  /*
   * ProductDbUtil is a seperate java class that
   * handles the direct communication with the database
   * and is not defined in this file
   */

  private ProductDbUtil productDbUtil;

	@Override
	public void init() throws ServletException {

    super.init();

		try {

			productDbUtil = new ProductDbUtil();
		}
		catch (Exception exc) {

			throw new ServletException(exc);
		}

	}


	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		try {

      /* Process the input from the user using request object */

			String theCommand = request.getParameter("command");

			if(theCommand == null) {

        /* Default action, if the user hasn't any particular request
         * display the content of the page dynamically, i.e. communicate
         * with the database and display the available products
         */

				theCommand = "LIST";
			}

      /* Perform an action based on the request */

			switch (theCommand) {

			case "LIST":
				addProduct(request, response);
				break;

			case "ADD":
				addProduct(request,response);
				break;

      /*
       * Define other actions, for example
       * case "FILTER", which based on the users filter
       * parameters, has to perform a query and display
       * the result of the query
       */

			default:
				listProduct(request, response);
				break;
			}

      /* After the action is performed, display all the products */

			listProduct(request,response);

		}
		catch(Exception exc) {
			throw new ServletException(exc);
		}

	}

    private void addProduct(HttpServletRequest request, HttpServletResponse response) throws Exception {

        /*
         * The user has added a new product
         * with several attributes which are accessible through
         * request object and in particular getParameter method
         */

        String name = request.getParameter("name"); // The value as it was entered from the user in the html form
        String company = request.getParameter("company");
        String category = request.getParameter("category");

        /* getParameter always returns a string type */

        /*
         * Float and Integer classes provide a method
         * to convert from string. Note that they would throw an Exception
         * if that value was null. However, in our case every field
         * is required from the user
         */

        float stars  = Float.parseFloat(request.getParameter("stars")); // Convert string to float

        Product theProduct = new Product(name, company, category, stars);

        /*
         * Create a new product based on the users input
         * and insert the product into the database
         * using ProductDbUtil class
         */

         /* If the insertion was completed successfully the method will return true */

        if (productDbUtil.addProduct(theProduct)) {

            // Display the appropriate message based on the outcome of the query

			       request.setAttribute("message", "Successful add");
		    }
        else {

			       request.setAttribute("message", "Unsuccessful add");
		    }



        listProduct(request, response);

    }

	private void listProduct(HttpServletRequest request,
							  HttpServletResponse response)
		throws Exception {


      /*
       * Query the database in getProducts method
       * and send the list of products in the frond-end
       * i.e. /product.jsp
       */

       /*
        * .jsp is based on HTML but it is used
        * to create dynamically generated web pages
        */

		List<Product> products = productDbUtil.getProducts();

		request.setAttribute("PRODUCT_LIST",products);

    /*
     * A RequestDispatcher performs a very important role
     * since it can serve as the mechanism for the servlet
     * to pass data to the view level, i.e. front-end.
     */

		RequestDispatcher dispatcher = request.getRequestDispatcher("/product.jsp");

    dispatcher.forward(request, response);

	}
}

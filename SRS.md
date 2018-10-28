## Introduction and purpose

This document was created at the inception of the project in order to specify the desired software characteristics and clarify the functional and business requirements. Due to the dynamic nature of every software development process, this document was modified and updated at every new step of the development process.

### Definitions

**Crowdsourcing:**

  The practice where a large number of people combine information to achieve a common goal, usually via the internet

### System overview

**Web application name, logo and motto:**

**What do we offer:**

*On an abstract level*

  Reliable, accurate, efficient, up-to-date, easy and fast to access information, secure transactions system and a chance to be a part of an interactive crowdsourcing community.

**What makes as different from existing services:**

**Main purpose:**

*On a technical level*

  Develop a web application that presents several information about different products, based on the input from the users, and allow the user to complete secure transactions.

**What types of products should we focus on:**

    i. General approach, include every possible product that is  registered by the users.
    ii. Focus on a certain type of products, for example:
      a. Technology
      b. Fashion
      c. Art
      d. Home and kitchen
      e. Automotive

**Should we focus on a certain group of people or adopt a more general approach?**


### References

**Our university**
National Technical University of Athens [https://www.ntua.gr]

**Our department**
Electrical engineering and computer science [https://www.ece.ntua.gr]

### Development choices

**Version control system tool: git**

Overwhelmingly the leading version control system tool. It's main characteristics are:

    a. Distributed development. Each developer get his own local repository that points to a single central repository.
    b. Non linear, branch workflow. Organizational benefits and ensuring the master branch always contains production quality code.
    c. Data integrity
    d. Efficiency compared to other DVCS
    e. Free and open source

**Build automation system: gradle**

## Overall description, product perspective and function

### Software description:

*On a high level*

Register and examine information on several products found on nearby stores through our web service. The application is based on the method of crowdsourcing. In our case, our goal is to keep track of the quality and the value of different products and optimize the customers choice.

## System and software interfaces

### User interfaces

*The user interface will be set in greek*

**Each registered user should have a profile page**

    What should we include in it?

    i. Personal information: full name, email, a sort bio and description, a profile image.
    ii. Registration history, the amount of registered and bought products. Each user should also possess a bonus card which will keep track of his activity.  

**How to ensure the quality of the presented information**

In order to present reliable information, each registration will have a review, so if it drops below a certain amount it will be automatically removed. In that way, we remove the biased and the info that is not up-to-date. In that sense, our page will have an autocorrection mechanism, based entirely on our users activity and feedback.  


## Communication interfaces

A registered user should be able to communicate with other users and the administrator, in order to send feedback and review several aspects of the application

## User characteristics

**Types of users**

    a. Registered user: update information via the user interface and the RESTful API
    b. Administrator: (Special type of password required) manage the accounts
    c. Visitor: query our database with several criteria, such as location, time stamp and type of products

  *General description*

**Users actions**

**a. Registered user**

    i. Perform visitors actions
    ii. Create an account
    iii. Connect using a username and an Password
    iv. Visit his profile page where he can manage and update his profile and delete his account
    v. Register products with a time stamp, a location, a value and possibly some general comments
    vi. Communicate with the administrator and other users and send feedback

**b. Administrator**

    i. Perform visitors actions
    ii. Verify administrator privileges through the use of a private key
    iii. Access users information and track their history
    iv. Delete, modify or restore user accounts after their request

    Should the administrator sign in from the users form or from a completely different one?  

**c. Visitor**

    i. Become a registered user
    ii. Query our database based on several criteria

**The search should be based on:**

    i. His location: Sort the results, in order to present the entries that are close to him
    ii. Key words: We need some sort of search engine
    iii. Categories and criteria
    iv. Time stamp

## Users attributes

**Registered User**

    i. User name: unique
    ii. Full Name: constraints?
    iii. Password: How many and which characters should be included
    iv. email address
    v. Address: City-Street-Number-Postal Code
    vi. Phone number: Not a mandatory field
    vii. Some form of authentication: An id like SSN that can serve as the primary key

**Administrator**

    i. User name: sysadmin
    ii. Private key that redirects to the administrator page

    Can he change his private key? 
    Other fields that are not significant <br/>
    The administrator is unique? <br/>
    Can he modify his private key? <br/>
    Can he add other administrators or delete his own account?

**Visitor**

  *Can a visitor perform a transaction?*
  If not, we are not interested in modeling his attributes

## Product attributes

    i. id: Serve as a primary key and is not visible to the user
    ii. Name: User visible identifier
    iii. Category: Drop down list
    iv. Manufacturer
    v. Value: Within a reasonable range
    vi. Time stamp
    vii. Store/Location

  <br/>*How do we check if a location is valid?* <br/>
  *In what form is the location given? Address form or google-maps entry for example* <br/>
  *Store attribute should probably be a foreign key* <br/>


## Stores

  <br/>*Should the store be a separate entity and if so what are its attributes?* <br/>
  *Can the user add a store in our database?*

## e-wallet

  *Better definition of the transaction process*

## Database info

**UML Diagram:**

  Define the entities, their dependencies and their relationship in general.

  For example:

      a. Entities: user, administrator, products, card...
      b. Relationships: user_is_registered, user_bought_product, user_registered_product, user_has_card...

*Should the administrator modify our database or it is entirely our users responsibility?*


## Constraints, assumptions and dependencies

**Time, budget and human resources constraints:**

  The development is taking place under a strict time limit of 3 months, with zero economical resources and by a group of 6. Therefore, we should prioritize our tasks and focus on the functionality of the application, which is the essential part.

**How do we make profit from the application?**

    a. Keep a percentage (5-10%) from the sales
    b. Use advertises, possibly with a dynamic approach based on the user preferences  

**The problem of parallel transactions, two or more people attempting to buy the same product.**

## Specific requirements

## Software system attributes

### Reliability:
  Present reliable information.
### Availability:
  The application has to be constantly available. Every request has to receive an answer, not necessarily the most recent one.   
### Security:
  Use the secure version of http, which is https and it means that all communications between the website and the browser are encrypted. In that way, customers information like credit card numbers cannot be intercepted and potential customers are more likely to trust use. To achieve that, security certificate (SSL) that can be verified by the certificate authorities must be installed. The use of https protocol, will also favor our application on search engines results
### Maintainability:
  After the completion of the project, a regular functional control of the application to detect any abnormalities.
### Portability:
  Mobile/tablet app or web service?

## ACID transactions
### Atomicity
### Consistency
### Isolation
### Durability  

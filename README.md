Crafted by Jimin
Feel free to ask any questions through E-mail: jiminsong.software@gmail.com

[Self Service Banking System]
App-Demo: [On-Progress]

=======

# Self Services Banking System!

I created two folders, each containing required codes to run separately.
One is for command line UI to batch test with code provided from Professor button
For Console Output folder, Bankingsystem.java contains 9 methods and P1.java contains command line UI codes. Also OpenAccount, CloseAccount, Transfer, etc... functions are in both P1 AND BankingSystem. It has been created in P1.java with same name to avoid confusion

I didn't change signature in BankingSystem.java
I returned value in P1.Java Which i created
And it was mentioned in the document, like OpenAccount should return account number if account opened successfully

In GUI folder, it contains Admin.java, CustomerGUI.java, MainPage.java, P2.java, and Bankingsystem.java.

You can change first two lines
'
argv = new String[1];
argv[0] = "db.properties";
'
if you are running code with command
'
javac P1.java
java P1 db.properties
'

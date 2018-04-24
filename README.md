# Masked Password
Masked Password implementation using the beauty of simple math.

### What is a masked password?
A way of authenticating where you only type some characters of your password instead of whole.

## Disclaimer
This is NOT a cryptographic library and the author is NOT a cryptography specialist.
The algorithm used here is NOT proven to have no vulnerability. 
 
I don't recommend using this code in production and can not guarantee it's safety.

## How does it work?

The main idea: if you have **n** points on a graph, you can generate a unique **(n - 1)**-degree polynomial from them.

### Password creation
 
We create a **n - 1** degree polynomial, where **n** is the amount of characters needed in 
single authentication, with coefficients formed from random 128-bit BigIntegers,
 let's call it **P(x)**.

Then for every character in the passphrase, we select a random point **p(x, y)**
 on the **P(x)**'s graph and subtract the 
  ASCII code of the character from the **y** coordinate of **p(x, y)**, so we make **P(x)** unresolvable
  from **p(x, y)** without the ASCII codes. 
  
Then we compute y-axis intersection = **P(0)** and save information about the intersection and selected points 
**(x, y - ASCII_i)** only. The rest of information is discarded. 

### Password verification
The authenticator provides character numbers to send back. 
To solve a **n**-degree polynomial we need at least
**n + 1** points/characters. So for example if we need to type 6 characters and our password is 12, we have 12 points
generated and the authenticator selects a random 6.

Similar like in the password creation, we take the points **p(x, y)** for corresponding 
characters, but now we add the ASCII code to complete the secret and get correct **y**-coordinate value again.

Then we can build a equation system and solve it to compute the original **P(x)** coefficients.

We generate the polynomial and evaluate it at **P(0)** to get the intersection value. By comparing it with the saved intersection, we can determine if authenticated person provided correct characters for 
their password.  
    

# Finding E-mail Addresses in a Corrupted File

## The E-mail Addresses

Email addresses have two parts to them, separated by the ‘**@**’ symbol: the <u>**local-part**</u> and the <u>**domain-part**</u>.

In the following examples, the string before the ‘**@**’ symbol is the local-part and the string after the ‘@’ is the domain-part:

sole_survivor@sanctuaryhills.com

axlrose6@gunsnroses.net 

gumball@amazing.world.of.jp



In the following description, the local-part and the domain-part will be described as having components. The local-part can have more than one component, separated by a separator. The domain part will have at least two components, separated by a separator.

The final part of the domain-part, which follows the final period (‘**.**’) character is called the *top-level domain*.

### The Format of the E-mail Addresses:

#### Local-part
The local-part will contain only:

* Upper-case and lower-case letters.

* The digits 0-9.

* The underscore character ‘**_**‘. This can appear anywhere in the local-part.

* The period ‘**.**’ character, with the restriction that this is only used as a *separator*. The period character cannot appear at the beginning or end of the local-part. There will be a maximum of two components to the local-part. For example, ‘gumball’ has one component whilst ‘bill.gates’ has two. There will be, therefore, at most one period character in the local-part.

#### Domain-part

  The domain-part will contain only:

* Lower-case letters.
* The period ‘**.**’ character, with the restriction that this is only used as a *separator*. Each domain-part will consist of either two or three components separated by a period.
* Top-Level Domain from the following set: {net, com, uk, de, jp, ro}.

## How the Program works:

It first reads all the e-mail addresses from "**corrupteddb**" and then based on the above rules and format it outputs all the correct e-mail addresses in a file called "**output.txt**".
# ADP: Patricia Tries vs. Hybrid Tries
A project for the course "Algorithmique Avancée" at the university UPMC.

## Introduction
For this project the two advanced data structures patricia trie and hybrid trie have been implemented and compared
concerning their suitability for the implementation of a common alphabetic dictionary consisting of the first
128 ASCII characters. A possible field of application for such a dictionary could be in terms of auto-completion
for mobile text messaging or the implementation of a simple spell checking dictionary.

Both trie types support several primitive and more sophisticated operations, including rather complex functions
for balancing, conversion and merging. Furthermore a small experimental study has been conducted: A patricia trie
and two different versions of a hybrid trie have been constructed by inserting the total of words contained
in all works of the English writer William Shakespeare into each trie. Some of these results can be found in
the folder ```results```.

## Patricia Trie
A patricia trie is a special form of a radix trie for which the edges between nodes can not only be labelled with
one character but also with a sequence of characters (i.e. prefixes) so that these structures are more space
efficient than regular tries. Furthermore the end of a stored word is no longer indicated by a given key in
a leaf node but rather by a special character that is added to the edge representing the last characters of a word.
As this “end of word” character serves as a special indicator, words containing it can not be stored or represented
by the dictionary.

As the used for alphabet for this work contains the first 128 characters of the character encoding standard ASCII,
the character DEL (With a decimal value of 127) was chosen to serve as the end of word character. This decision
seemed reasonable as the DEL character is a special character that is seldom used in spoken or written languages
as it is rather a computer-specific term.

## Hybrid Trie
The hybrid trie, also sometimes called digital tree or prefix tree, is a data structure which is able to handle
effectively  digital as well as string inputs. Each node has at always exactly three child nodes, conventionally
known as lower (left) child, middle child and higher (right) child. Their main characteristic is that the value of
the parent node is limited between the value of the lower and the higher child node so that the infix traversal
returns the elements in an ascending order.

## Examples


## Graphical Representation
For a visual representation of the graphs, the language [xdot](https://github.com/jrfonseca/xdot.py) was used. Some example files for both trie structures can be found in the folder ```drawables```.






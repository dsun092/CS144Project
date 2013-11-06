We chose to only index itemID, name, and description attribute of Item because those are the only attributes required for the reverse index. We decided to only store itemID and Name because they are only required for output. In addition, we tokenized description but did not store it. 

On the MySQL index, we decided to index the rest of the attributes for our search because it is easier. 

TODO: FIX Lucene Index to contain correct output. 
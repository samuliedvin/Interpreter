"Give_A(=0),_O(=1),_K(=2),or_P(=3)" print read 

// Two slashes at the end of line marks comments //



dup 0 ==
if 50 0 0 100 "black" line 50 0 100 100 "black" line 20 50 80 50 "black" line 0 else drop

dup 1 ==
if 25 25 100 "black" circle 1 else drop
  
dup 2 ==
if 5 0 5 100 "black" line 5 50 50 0 "black" line 5 50 50 100 "black" line 2 else drop

dup 3 ==
if 5 0 5 100 "black" line 5 0 5 50 50 25 "black" triangle 3 else drop


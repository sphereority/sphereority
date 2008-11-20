#!/usr/bin/python

import random

name = raw_input("What do you want to call the file? (default is new_map): ")

width = raw_input("What width do you want the file? (default is 50): ")
height = raw_input("What height? (default is 50): ")
if width == "":
    width = 50
else:
    width = int(width)
if height == "":
    height = 50
else:
    height = int(height)


out_file = open(name + ".map", "w")
out_file.write("%d %d\n"%(width, height))

for y in xrange(height):
    for x in xrange(width):
        if x == 0 or y == 0:
            out_file.write("+")
        elif x+1 == width or y+1 == height:
            out_file.write("+")
        elif random.random() > 0.75:
            out_file.write("+")
        else:
            out_file.write(".")
    out_file.write("\n")

out_file.close()

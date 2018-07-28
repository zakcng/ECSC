CC=gcc

libHelloWorld.so: HelloWorld.o
	gcc -shared -o libHelloWorld.so HelloWorld.o

HelloWorld.o: HelloWorld.c main_HelloWorld.h
	gcc -I/usr/lib/jvm/java-8-openjdk-amd64/include -I/usr/lib/jvm/java-8-openjdk-amd64/include/linux -c -Wall -Werror -fpic HelloWorld.c

clean:
	rm *.o libHelloWorld.so

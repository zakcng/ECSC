CC=gcc

libPasswordManager.so: PasswordManager.o
	gcc -shared -o libPasswordManager.so PasswordManager.o

PasswordManager.o: PasswordManager.c main_PasswordManager.h
	gcc -I/usr/lib/jvm/java-8-openjdk-amd64/include -I/usr/lib/jvm/java-8-openjdk-amd64/include/linux -c -Wall -Werror -fpic PasswordManager.c

clean:
	rm *.o libPasswordManager.so

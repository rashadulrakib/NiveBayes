JFLAGS	=	-g
JC	=	javac
.SUFFIXES:	.java	.class
.java.class:
	$(JC)	$(JFLAGS)	$*.java

CLASSES	=	\
	BaysClssifier/BC.java	\
	BaysClssifier/Classification.java	\
	BaysClssifier/PriorCountEntity.java	\
	BaysClssifier/Transaction.java	\
	BaysClssifier/TransactionEntity.java	\
	BaysClssifier/Util.java	

default:	classes

classes:	$(CLASSES:.java=.class)

clean:
		$(RM)	*.class

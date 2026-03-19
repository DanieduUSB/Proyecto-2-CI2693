JAR_NAME = yugi.jar
KOTLINC = kotlinc
SOURCES = $(wildcard *.kt)
CLASSES = $(SOURCES:.kt=.class)

$(JAR_NAME): $(SOURCES)
	$(KOTLINC) $(SOURCES) -include-runtime -d $(JAR_NAME)

clean:
	rm -f $(JAR_NAME)
	rm -rf META-INF

.PHONY: clean

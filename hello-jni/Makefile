TOOLCHAIN = /Users/amon/grive/development/Android/NativeToolchain
GCC = $(TOOLCHAIN)/bin/aarch64-linux-android-gcc
CXX_FLAGS = -march=armv8-a --sysroot=$(TOOLCHAIN)/sysroot

all: build deploy 

.PHONY : build
.PHONY : deploy
.PHONY : clean

deploy :
	adb install -r ./bin/hellojni.apk

build : ./bin/hellojni.apk

./bin/%.apk :  ./bin/%.signed.apk
	$(ANDROID_HOME)/build-tools/23.0.3/zipalign -v -f 4 $< $@
	
./bin/%.signed.apk :  ./bin/%.unsigned.apk ./ToyKey.keystore
	$(JAVA_HOME)/bin/jarsigner -verbose -keystore ./ToyKey.keystore -storepass armena -keypass armena -signedjar $@ $< helljniKey
		
./bin/%.unsigned.apk : ./bin/classes.dex ./lib/arm64-v8a/libhello.so
	$(ANDROID_HOME)/build-tools/23.0.3/aapt package -v -f -M ./AndroidManifest.xml -S ./res \
	-I $(ANDROID_HOME)/platforms/android-23/android.jar -F $@ ./bin
	$(ANDROID_HOME)/build-tools/23.0.3/aapt add $@ lib/arm64-v8a/libhello.so
		
./bin/classes.dex : ./obj/com/example/hellojni/HelloJni.class
	$(ANDROID_HOME)/build-tools/23.0.3/dx --dex --verbose --output=$@ ./obj
	
./lib/arm64-v8a/libhello.so : ./jni/hello.o
	$(GCC) -shared $< -o $@
	
./jni/%.o : ./jni/%.c
	$(GCC) $(CXX_FLAGS) -fPIC -c $< -o $@
		
./src/com/example/hellojni/R.java : $(shell find ./res -type f)
	$(ANDROID_HOME)/build-tools/23.0.3/aapt package -v -f -m  -S ./res -J ./src -M ./AndroidManifest.xml \
	-I $(ANDROID_HOME)/platforms/android-23/android.jar

./obj/com/example/hellojni/HelloJni.class : ./src/com/example/hellojni/HelloJni.java  ./src/com/example/hellojni/R.java
	$(JAVA_HOME)/bin/javac -d ./obj -classpath $(ANDROID_HOME)/platforms/android-23/android.jar -sourcepath ./src $<
	
./ToyKey.keystore : 
	$(JAVA_HOME)/bin/keytool -genkeypair -validity 1000 -dname "CN=some company,O=Android,C=JPN" -keystore $@ \
	-storepass armena -keypass armena -alias helljniKey -keyalg RSA -v
	
clean:
	rm -f ./bin/* ./lib/arm64-v8a/* 
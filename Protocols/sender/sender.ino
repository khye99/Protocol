unsigned long currentTime = 0;
unsigned long previousTime = 0;
unsigned long delta = 1000;

const int pot = A1;
const int temp = A0;

void setup() {
  Serial.begin(9600);
  analogReference(DEFAULT);
  pinMode(pot, INPUT);
  pinMode(temp, INPUT);
}

void loop() {
   // put your main code here, to run repeatedly:
  currentTime = millis();

  if(currentTime > previousTime) {
    sendWrong();
    sendTimestamp(currentTime);      
    sendDebug("super");             
    sendPot(analogRead(pot));          
    sendError();                       
    sendTemperature(analogRead(temp));   
    previousTime += delta;
  }
}

void sendPot(int pot) {
  Serial.write(0x21);
  Serial.write(0x33);
  //Serial.write(2);
  Serial.write(pot>>8);
  Serial.write(pot);
}

void sendTemperature(int temp){
  Serial.write(0x21);
  Serial.write(0x34);
  //Serial.write(sizeof(temp));
  //Serial.write(temp);
  Serial.write(temp>>8);
  Serial.write(temp);
}

void sendTimestamp(unsigned long timestamp) {
  Serial.write(0x21);
  Serial.write(0x32);
  //Serial.write(sizeof(timestamp));
  Serial.write(timestamp>>24);
  Serial.write(timestamp>>16);
  Serial.write(timestamp>>8);
  Serial.write(timestamp);
}

void sendDebug(char* c) {
  Serial.write(0x21);
  Serial.write(0x30);
  Serial.write(strlen(c)); //had to add a size parameter because sizeof can't do its job apparently
  Serial.write(c);
}

void sendError() {
  if(analogRead(pot) > 500) {
    Serial.write(0x21);
    Serial.write(0x31);
    //Serial.write(16);
    Serial.write("***HIGH ALERT***");
  }
}

void sendWrong() {
  Serial.write(0x21);
  Serial.write(0x40);
}

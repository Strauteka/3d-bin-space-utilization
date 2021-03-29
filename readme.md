# 3d-bin-space-utilization

## Square space utilization core for 3d bin packing algorithm.

When starting to write algorithm for <strong>`3d Container loading software`</strong> it is hard to understand how to utilize space while adding cargo to it. I present you my algorithm of space utilization for 3d container packing software.  
<span style="color:red"><strong>Important! </strong></span>
<strong>Library does not provide useful packing algorithm:</strong>

- Demo packing algorithm is only to overview code usage, it helps to start writing your own container packing algorithm.
- Although it gives you solid results, each time it will give you different solution for the same problem.
- java3d library and Draw3d class gives you quick look / development tool for your algorithm results.

Notes:

- Coordinates for `Bin` starts at (`0,0,0`):[`length, height, width`] and goes till defined bin size.

- Each time, when you add `Cargo` to `Bin` you have to collect new `Bin.emptySpace()` list to know latest
  information of empty spaces in `Bin`.

- Core does not provide any restrictions when adding cargo, it provides information of empty spaces.

- Cargo can overlap if wrongly added.

- In `Bin`, `Cargo` rotates at most on six positions / sides.
  ![draw-demo1.jpg](./resources/draw-demo2.png)

- Detailed info
  - Run demo code.
  - View demo algorithm: https://github.com/Strauteka/3d-bin-space-utilization/blob/main/demo/src/main/java/org/strauteka/jbin/demo/algorithm PackerEntry.java class

# Usage

- Clone repository
- Compile: `mvn clean install`
- Run Demo: `java -jar .\demo\target\demo-jar-with-dependencies.jar`

## Todo:

- released first version

## Preview core

### Empty space determination

![draw-demo.jpg](./resources/draw-demo.png)

### Over-stack space

![draw-demo4.jpg](./resources/draw-demo4.png)

## Preview demo algorithm

### 5 types of items.

![draw-demo3.jpg](./resources/draw-demo3.png)

### 20 types of items.

![draw-demo1.jpg](./resources/draw-demo1.png)

### 50 types of items.

![draw-demo1.jpg](./resources/draw-demo5.png)

## Problems with `java3d` libraries?

You may find solution in java3d folder ;)

# 3d-bin-space-utilization

## Square space utilization core for 3d bin packing algorithm.

When starting to write algorithm for <strong>`3d Container loading software`</strong> it is hard to understand how to utilize space while adding cargo to it. I give you my algorithm / structure of space utilization for 3d container packing software.

- Demo packing algorithm gives you a very good results and understanding about code usage.
- Algorithm based on Random and little bit of magic :) Unfortunately, each time it will give you different solution for the same problem.
- java3d library and Draw3d class gives you quick look / development tool for your algorithm results.

Notes:

- Coordinates for `Bin` starts at (`0,0,0`):[`length, height, width`] and goes till defined bin size.

- Each time, when adding `Cargo` to `Bin` you have to create `new unmodifiable Bin` with added cargo to it (Example in demo algorithm).

- Core does not provide any restrictions when adding cargo, cargo can overlap, if not respected empty space.

- In `Bin`, `Cargo` rotates at most on six positions / sides.
  ![draw-demo1.jpg](./resources/draw-demo2.png)

- Detailed info
  - Run demo code.
  - Explore demo algorithm: https://github.com/Strauteka/3d-bin-space-utilization/blob/main/demo/src/main/java/org/strauteka/jbin/demo/algorithm PackerEntry.java class

# Usage

- Clone repository
- Compile: `mvn clean install`
- Run Demo: `java -jar .\demo\target\demo-jar-with-dependencies.jar`

# Requires

- Java 1.8
- Maven

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

![draw-demo5.jpg](./resources/draw-demo5.png)

### 100 types of items.

![draw-demo6.jpg](./resources/draw-demo6.png)

### Pallet support.

![draw-demo7.jpg](./resources/draw-demo7.png)

## Problems with `java3d` libraries?

You may find solution in java3d folder ;)

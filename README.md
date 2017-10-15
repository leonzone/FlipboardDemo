# Flipboard 动画

HenCoder「仿写酷界面」活动——征稿 之 Flipboard

**效果**

![效果](https://ws2.sinaimg.cn/large/006tKfTcly1fkeccnawrqg30dm0c0akj.gif)

**核心代码**

```java       
       //第一部分,此处牺牲了绘制效率，简化了代码逻辑
        canvas.save();
        camera.save();
        canvas.translate(centerX, centerY);
        canvas.rotate(-degree2);
        camera.rotateY(degree3);
        camera.applyToCanvas(canvas);
        canvas.clipRect(-centerX, -centerY, 0, centerY);
        canvas.rotate(degree2);
        canvas.translate(-centerX, -centerY);
        camera.restore();
        canvas.drawBitmap(bitmap, x, y, paint);
        canvas.restore();

        //第二部分
        canvas.save();
        camera.save();
        canvas.translate(centerX, centerY);
        canvas.rotate(-degree2);
        camera.rotateY(degree1);
        camera.applyToCanvas(canvas);
        canvas.clipRect(0, -centerY, centerX, centerY);
        canvas.rotate(degree2);
        canvas.translate(-centerX, -centerY);
        camera.restore();
        canvas.drawBitmap(bitmap, x, y, paint);
        canvas.restore();
```



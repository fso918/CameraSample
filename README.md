# Android可以直接使用于项目中的照相机示例
一个可以直接在项目中使用的Camera示例，包括照相机的基本使用，自动对焦，使用取景框等功能。同时，也是学习照相机开发中相关参数设置的很好的示例。
## 项目特色
- 照相机工能组件化，可以当做一个通用工能引入项目，实现解耦
- RectCameraMaskView支持属性自定义，可以按需实现各种不同的蒙版效果
- 实现了自动对焦工能
- 代码较规范，注解很详细
- 使用很灵活，可以直接使用已有的CameraActivity实现基础工能，也可以继承AbstractCameraActivity，实现onPictureComplete()方法，实现自己的业务工能，还可以自定义拍照的UI，只使用CameraHelper里面的工能。使用都很方便。


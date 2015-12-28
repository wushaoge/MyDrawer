# MyDrawer
手撸一个slidingdrawer简易库,实现基本功能

以下为自定义属性

  <declare-styleable name="MyDrawer">
        <attr name="content" format="reference" /> //主体view  必须指定
        <attr name="drag" format="reference" />    //拖动view  必须指定
        <attr name="handler" format="reference" /> //手柄view  必须包含在drag里面，可以为空
        <attr name="isOpen" format="boolean" /> //是否打开     初始化的时候是否打开
        <attr name="dragtype"/> //拖动方向
    </declare-styleable>


    <attr name="dragtype">
        <enum name="left" value="0"/> //从左边滑出来
        <enum name="right" value="1"/> //从右边滑出来
        <enum name="top" value="2"/> //从顶部滑出来
        <enum name="bottom" value="3"/> //从底部滑出来
    </attr>


  回调函数
  public interface MyDrawerListener{
  
          public void open(); //打开
  
          public void close(); //关闭
  
      }

# Snake-master
编译原理项目Snake，leakcannary ，AOP


注意事项
（1）在导入项目之后，由于LeakCanary是jar包呈现的，里面全是class文件。我们需要用LeakCanary源码进行替换，打开jar中某一个class文件，AndroidStudio会让你ChooseSource替换掉。
（2）点击ChooseSrouce，并手动定位到本地的相应源码所在文件夹（和jar里面结构一致），点击确定即可。（保证除R、BuildConfig之外的文件，在AndroidStudio中打开为java文件）
（3）需要替换的jar包有leakcanary-watcher-1.5.1、leakcanary-andriod-1.5.1、leakcanary-analyzer-1.5.1。
（4）leakcanary源码地址：https://github.com/square/leakcanary

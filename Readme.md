版本控制搞半天
webapp 下的静态文件太大了,想忽略,但是已经commit 了

首先回退版本 git reset --hard HEAD

对于已经 add 进缓存区的文件,需要使用
> git rm -r "目录路径"

删除暂存区的对应文件 , 然后commit 提交

还有那个 gitignore 插件,右键加入后是`!/src/man/webapp`

这是不忽略这个文件夹,我裂开.然后更改 gitignore 文件忽略webapp文件夹

然后就不会 add 进暂存区了,就可以愉快的push了

git这东西,多用,多遇到问题解决就ok了

-_- 心累 !!! 

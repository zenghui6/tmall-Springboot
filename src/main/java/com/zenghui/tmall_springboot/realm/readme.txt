Shiro

sale : 盐
    md5加密的缺点:密码一样 md5的值是一样的
    加盐后

Realm : 域
    Realm 这个单词翻译为 域，其实是非常难以理解的。
    域 是什么鬼？和权限有什么毛关系？ 这个单词Shiro的作者用的非常不好，让人很难理解。
    那么 Realm 在 Shiro里到底扮演什么角色呢？
    当应用程序向 Shiro 提供了 账号和密码之后， Shiro 就会问 Realm 这个账号密码是否对， 如果对的话，其所对应的用户拥有哪些角色，哪些权限。
    所以Realm 是什么？ 其实就是个中介。 Realm 得到了 Shiro 给的用户和密码后，有可能去找 ini 文件，就像Shiro 入门中的 shiro.ini，也可以去找数据库，就如同本知识点中的 DAO 查询信息。

    Realm 就是干这个用的，它才是真正进行用户认证和授权的关键地方。

session:   shiro 提供session;
建议在开发中，Controller层使用原生的HttpSession对象，在Service层中使用Shiro提供的Session对象。
如果在Service层中使用HttpSession对象，那么属于侵入式，并不建议这么做。Shiro提供的Session能够很好的解决这个问题。
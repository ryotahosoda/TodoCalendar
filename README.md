1. 使用した技術要素  
言語:java,HTML,CSS  
フレームワーク:Spring Boot  
データベース:MySQL  
ネットワーク:localhost  
その他:Wed,thymeleaf,DevTools 

2. 全体の設計・構成についての説明
ソースコードはSpring Initializerでひな型を作成しました。  
src/main/java/com/example/demo配下に  
DemoApplication.java  
Todo.java  
TodoController.java  
TodoRepository.java  
TodoService.java  
AppConfigureAdopter.java  
の6つのファイルを置いています。  
DemoApplication.javaはメイン関数を呼び出すためのものです。  
Todo.javaは、データベースを利用するためにEntitiyアノテーション、Idアノテーションを付与させたクラスを作成しています。中身はgetter/setterになっています。  
TodoController.javaは,コントローラクラスを生成しています。RequestMappingアノテーションでマッピングし、トップ画面、編集画面、検索画面に遷移させています。各画面で、エラー入力があった場合はメッセージを表示させます。また、新しくToDoを追加する場合、編集する場合の入力で特殊文字があるとエスケープさせます。
TodoRepository.javaは、データベースを扱うためのレポジトリクラスを生成しています。  
TodoService.javaは、データベースに変更等を加えるための関数を生成しています。
AppConfigureAdopter.javaは、messages_ja.propertiesを扱うためにConfigurationアノテーションを付与しています。
src/main/resources/templates配下に  
base.html  
edit.html  
index.html  
search.html  
の４つのファイルを置いています。  
base.htmlは、共通ヘッダーです。トップ、検索画面に遷移します。  
edit.htmlは、編集画面です。編集ボタンがクリックされたら、ここに遷移して編集を行います。ToDo名と期限が予め入力フォームにあり、それを変更することができます。期限は年月日で入力させます。  
index.htmlはトップ画面です。todo名、期限を入力してデータベースに書き込みます。下部にデータベースの内容を全て表示しています。
search.htmlは検索画面です。入力フォームに検索ワードを入力すると、ToDo名であいまい検索を行います。検索が見つからなかった場合はその旨を表示します。見つかった場合は件数を表示して、内容も表示します。未完了ボタンを押すと完了ボタンに切り替えます。 
データベース設計は、export.sqlの-- Table structure for table `todo`以下の部分です。上の部分は無視してください。　　

3. 開発環境のセットアップ手順  
javaのバージョン:1.8.0_221  
JDKのバージョン:jdk1.8.0_221  
開発環境:IntelliJ IDEA Community Edition 2019.1.3  
MySQLのバージョン:8.0  
開発環境にはIntelliJ IDEA　Community Edition 2019.1.3を利用しています。  
Jetbrainsのサイトより無料版をダウンロードし、インストールウィザードに従いインストールしました。その後、oracleのサイトよりJDKをインストールしました。  



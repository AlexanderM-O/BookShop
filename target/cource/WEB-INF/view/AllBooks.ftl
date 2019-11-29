<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>All Books</title>
</head>
<body>
    <h1>List of all books</h1>
    <p><a href="CreateBook">ADD NEW BOOK</a></p>
    <table>
        <tr>
            <td>Id</td>
            <td>Name</td>
            <td>Change book</td>
            <td>Delete book</td>
        </tr>

        <#list books as book>
        <tr>
            <td>${book.id}</td>
            <td>${book.name}</td>
            <td><a href="ChangeBook/${book.id}">Change</a></td>
            <td><a href="DeleteBook/${book.id}">Delete</a></td>
            <!--Sending book id as path variable-->
        <tr>
        </#list>
    </table>
</body>
</html>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Input new parameters</title>
</head>
	<body>
	<!-- GO BACK AS WE ARE ON COURSE/CHANGEbOOK/book.id    -->
		<form name="NewBook" action="../BookIsChanged/${book.id}" method="post">
		    <p>Name of book</p>
		    <input title="Name" type="text" name="name" value="${book.name}" >
		    <#if nameError??>
          		<div style="color: #ff0000;">
          	     ${nameError}
          		</div>
     		</#if>
		    <p>Chose genre of book</p>                                           
		    <#list AllGenres as genre>
		        <br>
		            <#assign itWas = false>
		            <#list book.stringsFromCheckedGenres as CheckedGenre>
		                <!-- CHECK WEATHER IT IS IN COLLECTION -->
		                <#if (CheckedGenre==genre)>
		                    <#assign itWas = true>
		                </#if>
		            </#list>
		            <input type="checkbox" name="EnumOfGenre" value="${genre}"
		                <#if (itWas==true)>checked<#else></#if>
		            >
		        <br>
		    ${genre}
		    </#list>
		    <p>Write names of authors of book(comma-separated list)</p>
		    <!-- Authors:string which will All related authors -->
		    <input title="Name" type="text" name="InputedAuthor" value="${book.stringFromAuthors}">
	  		<#if authorsError??>
            	<div style="color: #ff0000;">
               		${authorsError}
            	</div>
            </#if>
		    <input type="submit" value="Change book">
		</form>
	</body>
</html>

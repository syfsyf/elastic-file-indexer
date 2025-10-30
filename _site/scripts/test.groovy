import groovyx.net.http.RESTClient
import groovy.util.slurpersupport.GPathResult
import static groovyx.net.http.Method.POST
import static groovyx.net.http.Method.GET
import static groovyx.net.http.Method.PUT
import static groovyx.net.http.Method.DELETE
import static groovyx.net.http.ContentType.URLENC
import static groovyx.net.http.ContentType.JSON
import groovy.json.*;
 // komentarz

// @Grab(group='org.codehaus.groovy.modules.http-builder', module='http-builder', version='0.5.2')


client=new RESTClient( 'http://localhost:9200/' )
//json = new groovy.json.JsonBuilder()

try {
client.delete(path:'files')
}
catch(e){}

def list=[]

def scanDir(dir,list)
{
	dir.eachFile{file->
		println file
		def data=[
			name:file.name,
			type:file.directory ? 'dir':'file',
			size:file.length(),
		]
		if(!file.directory)
			data.path=file.parentFile.absolutePath
		else
			scanDir(file,list)
		
		list.add ([index:[_index:'files',_type:'file']])
		list.add data	
	}
}

scanDir(new File('d:/progs/groovy'),list);

/*
new File('d:/progs/groovy/lib').eachFile {file->
	
	def data=[
		name:'XXXX:'+file.name,
		type:file.directory ? 'dir':'file',
		size:file.length(),
	]
	if(!file.directory)
		data.path=file.parentFile.absolutePath
	
	list.add ([index:[_index:'files',_type:'file']])
	list.add data
	
		
}
*/

def strings=[]
list.each {
	strings.add JsonOutput.toJson(it)
}
println strings.join("\n")

client.post(path:'_bulk',body:strings.join("\n"),requestContentType:'text/html');


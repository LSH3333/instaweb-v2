// import { SimpleUploadAdapter } from '../../../../../../node_modules/@ckeditor/ckeditor5-upload';

// 에디터 객체 
let editor;

// 클래식 에디터 객체 생성 
ClassicEditor
    .create(document.querySelector('#editor'), {
        // plugins: [ SimpleUploadAdapter]
    })
    .then(newEditor => {
        editor = newEditor;
    })
    .catch(error => {
        console.error(error);
    });


// submit 
document.querySelector('#submit').addEventListener('click', () => {
    const editorData = editor.getData();
    console.log(editorData)

    pressSubmitBtn(editorData);
});

async function pressSubmitBtn(editorData) {
    await uploadToServer(editorData);
}

function uploadToServer(editorData) {
    return new Promise(function (resolve, reject) {
        const formData = new FormData();

        formData.append("content", editorData);
        
        const uploadToServerXhr = new XMLHttpRequest();
        uploadToServerXhr.open("POST", "/page/upload", true);
        uploadToServerXhr.onload = function () {
            if (uploadToServerXhr.status == 200) {
                console.log("Files uploaded successfully");
                resolve();
            } else {
                reject(new Error("Error upload files"));
            }
        };

        uploadToServerXhr.onerror = function () {
            reject(new Error("Network error"));
        };

        uploadToServerXhr.send(formData);
    })


}
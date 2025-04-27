function uploadFile() {
  const fileInput = document.getElementById('fileInput');
  const file = fileInput.files[0];

  if (!file) {
    alert("Please select a file.");
    return;
  }

  if (file.type !== "text/plain") {
    alert("Only .txt files are allowed.");
    return;
  }

  if (file.size > 10240) {
    alert("File size must be 10KB or less.");
    return;
  }

  const formData = new FormData();
  formData.append("file", file);

  fetch('/files/uploadDoc', {
    method: 'POST',
    body: formData
  })
  .then(response => response.text())
  .then(data => alert("Upload successful: " + data))
  .catch(error => alert("Upload failed: " + error));
}

function docAssistant() {
  const userText = document.getElementById('userText').value;

  if (!userText.trim()) {
    alert("Please enter a question.");
    return;
  }

  fetch(`/docAssistant?text=${userText}`)
    .then(response => response.text())
    .then(data => {
      document.getElementById('responseArea').value = data;
    })
    .catch(error => {
      document.getElementById('responseArea').value = "Error: " + error;
    });
}

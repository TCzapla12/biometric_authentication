namespace BusProject.Server.Rest
{
    using BusProject.Server.Logic;
    using BusProject.Server.Model;
    using BusProject.Server.Rest.Model;
    using Microsoft.AspNetCore.Http;
    using Microsoft.AspNetCore.Mvc;
    using Microsoft.Extensions.Logging;
    using System.IO;
    using System.Linq;
    using System.Threading.Tasks;

    [ApiController]
    [Route("[controller]")]
    public class FileController : ControllerBase, IFileService
    {
        private readonly ILogger<FileController> logger;

        private readonly IFileDataBase fdb;
        public FileController(IFileDataBase fdb, ILogger<FileController> logger)
        {
            this.logger = logger;
            this.fdb = fdb;
        }

        [HttpGet("Download/{id}")]
        public async Task<IActionResult> Download(string id)
        {
            if (this.fdb.FileExist(id))
            {
                string filepath = Path.Combine(this.fdb.GetPath(), id);
                byte[] file = await System.IO.File.ReadAllBytesAsync(filepath);
                return File(file, "application/octet-stream");
            }
            return StatusCode(StatusCodes.Status500InternalServerError);
        }

        [HttpPost("Upload")]
        public async Task<IActionResult> Upload(IFormFile file)
        {
            if(!this.fdb.FileExist(file.FileName))
            {
                string filePath = Path.Combine(this.fdb.GetPath(), file.FileName);
                using (var stream = new FileStream(filePath, FileMode.Create))
                {
                    await file.CopyToAsync(stream);
                }
                FileInfo fileInfo = new FileInfo(filePath);
                this.fdb.AddFile(fileInfo.Name, fileInfo.Length, fileInfo.CreationTime);
                return Ok("File has been uploaded");
            }
            return StatusCode(StatusCodes.Status500InternalServerError);  
        }

        [HttpDelete("Delete/{id}")]
        public async Task<IActionResult> Delete(string id)
        {
            if(this.fdb.FileExist(id))
            {
                this.fdb.DeleteFile(id);
                string filePath = Path.Combine(this.fdb.GetPath(), id);
                System.IO.File.Delete(filePath);
                await FileExtensions.DeleteAsync(new FileInfo(filePath));
                return Ok("File has been deleted");
            }
            return StatusCode(StatusCodes.Status500InternalServerError);
        }

        [HttpGet("Show")]
        public async Task<IActionResult> Show()
        {
            ServerFile[] serverFiles = this.fdb.GetFiles().ToArray();
            return Ok(this.fdb.GetFiles().Select(serverFiles => serverFiles.ConvertToFileData()));
        }
    }
}

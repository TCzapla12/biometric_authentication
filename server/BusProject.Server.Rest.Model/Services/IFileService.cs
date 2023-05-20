

namespace BusProject.Server.Rest.Model
{
    using BusProject.Server.Model;
    using Microsoft.AspNetCore.Http;
    using Microsoft.AspNetCore.Mvc;
    using System.Threading.Tasks;
    public interface IFileService
    {
        public Task<IActionResult> Download(string id);
        public Task<IActionResult> Upload(IFormFile file);
        public Task<IActionResult> Delete(string id);
        public Task<IActionResult> Show();
        

    }
}

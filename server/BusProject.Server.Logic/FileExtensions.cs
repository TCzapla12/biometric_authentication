namespace BusProject.Server.Logic
{
    using System.IO;
    using System.Threading.Tasks;
    public static class FileExtensions
    {
        public static Task DeleteAsync(this FileInfo fi)
        {
            return Task.Factory.StartNew(() => fi.Delete());
        }
    }
}

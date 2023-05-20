namespace BusProject.Server.Rest.Model
{
    using BusProject.Server.Model;

    public static class DataConverter
    {
        public static FileData ConvertToFileData(this ServerFile file)
        {
            return new FileData() { Id = file.Id, Size = file.Size, Date = file.Date.Date.ToString().Split(" ")[0]};
        }
    }
}

namespace BusProject.Server.Model
{
    using System;
    using System.Collections.Generic;
    public interface IFileDataBase
    {
        public List<ServerFile> GetFiles();
        public void AddFile(string id, double length, DateTime time);
        public void DeleteFile(string id);
        public string GetPath();
        public bool FileExist(string id);
    }
}
